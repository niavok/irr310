package com.irr310.i3d.view;

public interface ViewParent {
    /**
     * Called when something has changed which has invalidated the layout of a
     * child of this view parent. This will schedule a layout pass of the view
     * tree.
     */
    public void requestLayout();
    
    /**
    * Returns the parent if it exists, or null.
    *
    * @return a ViewParent or null if this ViewParent does not have a parent
    */
   public ViewParent getParent();

   public void addViewInLayout(View view);
   
   //public Layout getLayout();
}
