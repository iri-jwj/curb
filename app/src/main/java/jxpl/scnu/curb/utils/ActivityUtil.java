package jxpl.scnu.curb.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseArray;



public class ActivityUtil {

    private static final SparseArray<Fragment> fragments = new SparseArray<>();
    private static final SparseArray<Fragment> fragmentsNotUse = new SparseArray<>();
    private static FragmentManager fragmentManagerInHome;
    private static int containerView;

    private static FragmentManager fragmentManagerNotHome;
    private static int containerViewNotHome;

    private static int currentKey = 0;

    public static void addFragmentNotInHomePage(@NonNull Fragment fragment) {
        addFragment(-3, fragment);
    }

    public static void addFragmentInHomePage(@NonNull Integer id, @NonNull Fragment fragment) {
        addFragment(id, fragment);
    }

    private static void addFragment(@NonNull Integer id, @NonNull Fragment fragment) {
        fragmentsNotUse.put(id, fragment);
        Log.d("addFrag", "addFragment: " + fragment.isAdded());
    }

    public static void setFragmentManagerNotHome(FragmentManager fragmentManagerNotHome) {
        ActivityUtil.fragmentManagerNotHome = fragmentManagerNotHome;
    }

    public static void setFragmentManagerInHome(FragmentManager para_fragmentManager) {
        fragmentManagerInHome = para_fragmentManager;
    }

    public static void setContainerViewNotHome(int containerViewNotHome) {
        ActivityUtil.containerViewNotHome = containerViewNotHome;
    }

    public static void setContainerView(int id) {
        containerView = id;
    }

    public static void setCurrentFragmentNotInHome() {
        Fragment fragment = fragmentsNotUse.get(-3);
        fragmentManagerNotHome.beginTransaction()
                .add(containerViewNotHome, fragment)
                .commit();
    }

    public static void setCurrentFragment(@NonNull Integer id) {
        Log.d("ActivityUntil", "setCurrentFragment:" + "currentKey" + currentKey);
        if (currentKey != id) {
            if (currentKey > 0 && id > 0) {
                Fragment fragment = fragments.get(currentKey);
                fragmentManagerInHome.beginTransaction()
                        .hide(fragment)
                        .commit();
            }
            currentKey = id;
            if (fragmentsNotUse.get(id) == null) {
                Log.d("ActivityUntil", "setCurrentFragment:Must Init fragmentNotUse before use ");
                return;
            }
            if (id > 0) {
                if (fragments.get(id) == null) {
                    Fragment fragment = fragmentsNotUse.get(id);
                    fragments.put(id, fragment);
                    fragmentManagerInHome.beginTransaction()
                            .add(containerView, fragment)
                            .commit();
                } else {
                    Fragment fragment = fragments.get(id);
                    fragmentManagerInHome.beginTransaction()
                            .show(fragment)
                            .commit();
                }
            }
        }
    }

    public static void removeFragmentNotHome() {
        fragmentsNotUse.remove(-3);
    }

    public static void removeFragment() {
        fragments.clear();
        currentKey = 0;
    }

    public static int getCurrentKey() {
        return currentKey;
    }
}
