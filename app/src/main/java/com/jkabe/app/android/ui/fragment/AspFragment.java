package com.jkabe.app.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.jkabe.app.android.R;
import com.jkabe.app.android.base.BaseFragment;

/**
 * @author: zt
 * @date: 2020/11/24
 * @name:AspFragment
 */
public class AspFragment extends BaseFragment {
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_asp, container, false);
            initView();
        }
        return rootView;
    }

    private void initView() {

    }

}
