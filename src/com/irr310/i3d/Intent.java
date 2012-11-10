package com.irr310.i3d;

import com.irr310.i3d.view.Activity;

public class Intent {

    Class<? extends Activity> activityClass;
    Bundle bundle;

    public Intent(Class<? extends Activity> activityclass) {
        activityClass = activityclass;
        bundle = null;
    }
    
    public Intent(Class<? extends Activity> activityclass, Bundle bundle) {
        activityClass = activityclass;
        this.bundle = bundle;
    }

    public Class<? extends Activity> getActivityClass() {
        return activityClass;
    }

    public Bundle getBundle() {
        return bundle;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((activityClass == null) ? 0 : activityClass.hashCode());
        result = prime * result + ((bundle == null) ? 0 : bundle.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Intent other = (Intent) obj;
        if (activityClass == null) {
            if (other.activityClass != null)
                return false;
        } else if (!activityClass.equals(other.activityClass))
            return false;
        if (bundle == null) {
            if (other.bundle != null)
                return false;
        } else if (!bundle.equals(other.bundle))
            return false;
        return true;
    }
    
    
}
