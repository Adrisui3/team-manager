package com.manager.payments.application.port.out;

import com.manager.payments.model.users.User;

public interface UserRepository {

    User save(User user);

}
