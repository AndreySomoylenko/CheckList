package ru.samsung.case2022.vcs;

import android.content.Context;

public interface VersionAgent {
    void init(Context context);

    void add(String item);

    boolean removeByName(String item);
    boolean removeByIndex(int index);
    void save();
}
