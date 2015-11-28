package com.example.user.biographics_tests;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

//public class YearActivity extends ViewGroup{
//
//    /** The amount of space used by children in the left gutter. */
//    private int mLeftWidth;
//
//    /** The amount of space used by children in the right gutter. */
//    private int mRightWidth;
//
//    /** These are used for computing child frames based on their gravity. */
//    private final Rect mTmpContainerRect = new Rect();
//    private final Rect mTmpChildRect = new Rect();
//
//    public YearActivity(Context context) {
//        super(context);
//    }
//
//    public YearActivity(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public YearActivity(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//
//    /**
//     * Ask all children to measure themselves and compute the measurement of this
//     * layout based on the children.
//     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int count = getChildCount();
//
//        // These keep track of the space we are using on the left and right for
//        // views positioned there; we need member variables so we can also use
//        // these for layout later.
//        mLeftWidth = 0;
//        mRightWidth = 0;
//
//        // Measurement will ultimately be computing these values.
//        int maxHeight = 0;
//        int maxWidth = 0;
//        int childState = 0;
//
//        // Iterate through all children, measuring them and computing our dimensions
//        // from their size.
//        for (int i = 0; i < count; i++) {
//            final View child = getChildAt(i);
//            if (child.getVisibility() != GONE) {
//                // Measure the child.
//                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
//
//                // Update our size information based on the layout params.  Children
//                // that asked to be positioned on the left or right go in those gutters.
//                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
//                if (lp.position == LayoutParams.POSITION_LEFT) {
//                    mLeftWidth += Math.max(maxWidth,
//                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
//                } else if (lp.position == LayoutParams.POSITION_RIGHT) {
//                    mRightWidth += Math.max(maxWidth,
//                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
//                } else {
//                    maxWidth = Math.max(maxWidth,
//                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
//                }
//                maxHeight = Math.max(maxHeight,
//                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
//                childState = combineMeasuredStates(childState, child.getMeasuredState());
//            }
//        }
//
//        // Total width is the maximum width of all inner children plus the gutters.
//        maxWidth += mLeftWidth + mRightWidth;
//
//        // Check against our minimum height and width
//        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
//        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
//
//        // Report our final dimensions.
//        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
//                resolveSizeAndState(maxHeight, heightMeasureSpec,
//                        childState << MEASURED_HEIGHT_STATE_SHIFT));
//    }
//
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//
//    }

//}
