package com.elena.habitTracker.areas.users.models.view;

import com.elena.habitTracker.areas.logs.models.view.ApplicationLogViewModel;
import org.springframework.data.domain.Page;

public class UsersPageViewModel {
    private Page<UserViewModel> users;

    private long totalPagesCount;

    public Page<UserViewModel> getUsers() {
        return users;
    }

    public void setUsers(Page<UserViewModel> users) {
        this.users = users;
    }

    public long getTotalPagesCount() {
        return totalPagesCount;
    }

    public void setTotalPagesCount(long totalPagesCount) {
        this.totalPagesCount = totalPagesCount;
    }
}
