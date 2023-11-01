package com.apploop.face.changer.app.helpers.onTouch

import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt

class ScaleGestureDetector(private val mListener: OnScaleGestureListener) {
    interface OnScaleGestureListener {
        fun onScale(view: View?, detector: ScaleGestureDetector?): Boolean
        fun onScaleBegin(view: View?, detector: ScaleGestureDetector?): Boolean
        fun onScaleEnd(view: View?, detector: ScaleGestureDetector?)
    }

    open class SimpleOnScaleGestureListener : OnScaleGestureListener {
        override fun onScale(view: View?, detector: ScaleGestureDetector?): Boolean {
            return false
        }

        override fun onScaleBegin(view: View?, detector: ScaleGestureDetector?): Boolean {
            return true
        }

        override fun onScaleEnd(view: View?, detector: ScaleGestureDetector?) {}
    }

    var isInProgress = false
        private set
    private var mPrevEvent: MotionEvent? = null
    private var mCurrEvent: MotionEvent? = null
    private val mCurrSpanVector: com.apploop.face.changer.app.helpers.onTouch.Vector2D =
        com.apploop.face.changer.app.helpers.onTouch.Vector2D()
    var focusX = 0f
        private set
    var focusY = 0f
        private set
    var previousSpanX = 0f
        private set
    var previousSpanY = 0f
        private set
    var currentSpanX = 0f
        private set
    var currentSpanY = 0f
        private set
    private var mCurrLen = 0f
    private var mPrevLen = 0f
    private var mScaleFactor = 0f
    private var mCurrPressure = 0f
    private var mPrevPressure = 0f
    private var timeDelta: Long = 0
    private var mInvalidGesture = false
    private var mActiveId0 = 0
    private var mActiveId1 = 0
    private var mActive0MostRecent = false
    fun onTouchEvent(view: View, event: MotionEvent): Boolean {
        val action = event.actionMasked
        if (action == MotionEvent.ACTION_DOWN) {
            reset()
        }
        var handled = true
        if (mInvalidGesture) {
            handled = false
        } else if (!isInProgress) {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    mActiveId0 = event.getPointerId(0)
                    mActive0MostRecent = true
                }
                MotionEvent.ACTION_UP -> reset()
                MotionEvent.ACTION_POINTER_DOWN -> {
//                    if (mPrevEvent != null) mPrevEvent?.recycle()
                    mPrevEvent = MotionEvent.obtain(event)
                    timeDelta = 0
                    val index1 = event.actionIndex
                    var index0 = event.findPointerIndex(mActiveId0)
                    mActiveId1 = event.getPointerId(index1)
                    if (index0 < 0 || index0 == index1) {
                        index0 = findNewActiveIndex(event, mActiveId1, -1)
                        mActiveId0 = event.getPointerId(index0)
                    }
                    mActive0MostRecent = false
                    setContext(view, event)
                    isInProgress = mListener.onScaleBegin(view, this)
                }
            }
        } else {
            when (action) {
                MotionEvent.ACTION_POINTER_DOWN -> {
                    mListener.onScaleEnd(view, this)
                    val oldActive0 = mActiveId0
                    val oldActive1 = mActiveId1
                    reset()
                    mPrevEvent = MotionEvent.obtain(event)
                    mActiveId0 = if (mActive0MostRecent) oldActive0 else oldActive1
                    mActiveId1 = event.getPointerId(event.actionIndex)
                    mActive0MostRecent = false
                    var index0 = event.findPointerIndex(mActiveId0)
                    if (index0 < 0 || mActiveId0 == mActiveId1) {
                        index0 = findNewActiveIndex(event, mActiveId1, -1)
                        mActiveId0 = event.getPointerId(index0)
                    }
                    setContext(view, event)
                    isInProgress = mListener.onScaleBegin(view, this)
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    val pointerCount = event.pointerCount
                    val actionIndex = event.actionIndex
                    val actionId = event.getPointerId(actionIndex)
                    var gestureEnded = false
                    if (pointerCount > 2) {
                        if (actionId == mActiveId0) {
                            val newIndex = findNewActiveIndex(event, mActiveId1, actionIndex)
                            if (newIndex >= 0) {
                                mListener.onScaleEnd(view, this)
                                mActiveId0 = event.getPointerId(newIndex)
                                mActive0MostRecent = true
                                mPrevEvent = MotionEvent.obtain(event)
                                setContext(view, event)
                                isInProgress = mListener.onScaleBegin(view, this)
                            } else {
                                gestureEnded = true
                            }
                        } else if (actionId == mActiveId1) {
                            val newIndex = findNewActiveIndex(event, mActiveId0, actionIndex)
                            if (newIndex >= 0) {
                                mListener.onScaleEnd(view, this)
                                mActiveId1 = event.getPointerId(newIndex)
                                mActive0MostRecent = false
                                mPrevEvent = MotionEvent.obtain(event)
                                setContext(view, event)
                                isInProgress = mListener.onScaleBegin(view, this)
                            } else {
                                gestureEnded = true
                            }
                        }
//                        mPrevEvent?.recycle()
                        mPrevEvent = MotionEvent.obtain(event)
                        setContext(view, event)
                    } else {
                        gestureEnded = true
                    }
                    if (gestureEnded) {
                        setContext(view, event)
                        val activeId = if (actionId == mActiveId0) mActiveId1 else mActiveId0
                        val index = event.findPointerIndex(activeId)
                        focusX = event.getX(index)
                        focusY = event.getY(index)
                        mListener.onScaleEnd(view, this)
                        reset()
                        mActiveId0 = activeId
                        mActive0MostRecent = true
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    mListener.onScaleEnd(view, this)
                    reset()
                }
                MotionEvent.ACTION_UP -> reset()
                MotionEvent.ACTION_MOVE -> {
                    setContext(view, event)
                    if (mCurrPressure / mPrevPressure > PRESSURE_THRESHOLD) {
                        val updatePrevious = mListener.onScale(view, this)
                        if (updatePrevious) {
//                            mPrevEvent!!.recycle()
                            mPrevEvent = MotionEvent.obtain(event)
                        }
                    }
                }
            }
        }
        return handled
    }

    private fun findNewActiveIndex(
        ev: MotionEvent,
        otherActiveId: Int,
        removedPointerIndex: Int
    ): Int {
        val pointerCount = ev.pointerCount
        val otherActiveIndex = ev.findPointerIndex(otherActiveId)
        for (i in 0 until pointerCount) {
            if (i != removedPointerIndex && i != otherActiveIndex) {
                return i
            }
        }
        return -1
    }

    private fun setContext(view: View, curr: MotionEvent) {
        if (mCurrEvent != null) {
//            mCurrEvent!!.recycle()
        }
        mCurrEvent = MotionEvent.obtain(curr)
        mCurrLen = -1f
        mPrevLen = -1f
        mScaleFactor = -1f
        mCurrSpanVector.set(0.0f, 0.0f)
        val prev = mPrevEvent
        val prevIndex0 = prev!!.findPointerIndex(mActiveId0)
        val prevIndex1 = prev.findPointerIndex(mActiveId1)
        val currIndex0 = curr.findPointerIndex(mActiveId0)
        val currIndex1 = curr.findPointerIndex(mActiveId1)
        if (prevIndex0 < 0 || prevIndex1 < 0 || currIndex0 < 0 || currIndex1 < 0) {
            mInvalidGesture = true
            Log.e(TAG, "Invalid MotionEvent stream detected.", Throwable())
            if (isInProgress) {
                mListener.onScaleEnd(view, this)
            }
            return
        }
        val px0 = prev.getX(prevIndex0)
        val py0 = prev.getY(prevIndex0)
        val px1 = prev.getX(prevIndex1)
        val py1 = prev.getY(prevIndex1)
        val cx0 = curr.getX(currIndex0)
        val cy0 = curr.getY(currIndex0)
        val cx1 = curr.getX(currIndex1)
        val cy1 = curr.getY(currIndex1)
        val pvx = px1 - px0
        val pvy = py1 - py0
        val cvx = cx1 - cx0
        val cvy = cy1 - cy0
        mCurrSpanVector.set(cvx, cvy)
        previousSpanX = pvx
        previousSpanY = pvy
        currentSpanX = cvx
        currentSpanY = cvy
        focusX = cx0 + cvx * 0.5f
        focusY = cy0 + cvy * 0.5f
        timeDelta = curr.eventTime - prev.eventTime
        mCurrPressure = curr.getPressure(currIndex0) + curr.getPressure(currIndex1)
        mPrevPressure = prev.getPressure(prevIndex0) + prev.getPressure(prevIndex1)
    }

    private fun reset() {
        if (mPrevEvent != null) {
//            mPrevEvent?.recycle()
            mPrevEvent = null
        }
        if (mCurrEvent != null) {
//            mCurrEvent?.recycle()
            mCurrEvent = null
        }
        isInProgress = false
        mActiveId0 = -1
        mActiveId1 = -1
        mInvalidGesture = false
    }

    private val currentSpan: Float
        get() {
            if (mCurrLen == -1f) {
                val cvx = currentSpanX
                val cvy = currentSpanY
                mCurrLen = sqrt((cvx * cvx + cvy * cvy).toDouble()).toFloat()
            }
            return mCurrLen
        }
    val currentSpanVector: com.apploop.face.changer.app.helpers.onTouch.Vector2D
        get() = mCurrSpanVector
    private val previousSpan: Float
        get() {
            if (mPrevLen == -1f) {
                val pvx = previousSpanX
                val pvy = previousSpanY
                mPrevLen = sqrt((pvx * pvx + pvy * pvy).toDouble()).toFloat()
            }
            return mPrevLen
        }
    val scaleFactor: Float
        get() {
            if (mScaleFactor == -1f) {
                mScaleFactor = currentSpan / previousSpan
            }
            return mScaleFactor
        }
    val eventTime: Long
        get() = mCurrEvent!!.eventTime

    companion object {
        private const val TAG = "ScaleGestureDetector"
        private const val PRESSURE_THRESHOLD = 0.67f
    }

}