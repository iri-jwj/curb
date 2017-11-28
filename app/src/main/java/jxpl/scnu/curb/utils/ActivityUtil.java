package jxpl.scnu.curb.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by irijw on 2017/10/29.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class ActivityUtil {

    private static final SparseArray<Fragment> fragments = new SparseArray<>();
    private static final SparseArray<Fragment> fragmentsNotUse = new SparseArray<>();
    private static FragmentManager fragmentManager;
    private static int currentKey=-1;
    private static int containerView;


    public static void addFragment(@NonNull Integer id , @NonNull Fragment fragment){
        fragmentsNotUse.put(id,fragment);
        Log.d("addFrag", "addFragment: "+fragment.isAdded());
    }
    public static void setFragmentManager(FragmentManager ifragmentManager){
        fragmentManager=ifragmentManager;
    }

    public static void setContainerView(int id){
        containerView=id;
    }

    public static void setCurrentFragment(@NonNull Integer id){
        if(currentKey!=id){
            if(currentKey>=0){
                Fragment fragment = fragments.get(currentKey);
                fragmentManager.beginTransaction()
                        .hide(fragment)
                        .commit();
                fragment.setUserVisibleHint(false);
            }
            currentKey=id;
            if (id >= 0) {
                if(fragmentsNotUse.get(id)==null){
                    Log.d("ActivityUntil", "setCurrentFragment:Must Init fragmentNotUse before use ");
                    return;
                }
                if (fragments.get(id)==null){
                    Fragment fragment=fragmentsNotUse.get(id);
                    fragments.put(id,fragment);
                    fragmentManager.beginTransaction()
                            .add(containerView,fragment)
                            .commit();
                    fragment.setUserVisibleHint(true);
                }else {
                    Fragment fragment = fragments.get(id);
                    fragmentManager.beginTransaction()
                            .show(fragment)
                            .commit();
                    fragment.setUserVisibleHint(true);
                }
            }
        }
    }
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();

    }
}
