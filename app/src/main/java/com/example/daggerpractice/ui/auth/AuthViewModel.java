package com.example.daggerpractice.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.daggerpractice.models.User;
import com.example.daggerpractice.network.auth.AuthAPI;

import javax.inject.Inject;

import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {
    private static final String TAG = "AuthViewModel";

    private final AuthAPI authAPI;

    private final MediatorLiveData<AuthResource<User>> authUser;

    @Inject
    public AuthViewModel(AuthAPI authAPI) {
        this.authAPI = authAPI;
        authUser = new MediatorLiveData<>();
    }

    public void authenticateWithId(int userId) {
        authUser.setValue(AuthResource.loading((User)null));

        final LiveData<AuthResource<User>> source = LiveDataReactiveStreams.fromPublisher(
                authAPI.getUser(userId)
                        .onErrorReturn(new Function<Throwable, User>() {
                            @Override
                            public User apply(Throwable throwable) throws Throwable {
                                User errorUser = new User();
                                errorUser.setId(-1);
                                return errorUser;
                            }
                        })
                        .map(new Function<User, AuthResource<User>>() {
                            @Override
                            public AuthResource<User> apply(User user) throws Throwable {
                                if (user.getId() == -1) {
                                    return AuthResource.error("Could not authenticate", (User) null);
                                }
                                return AuthResource.authenticated(user);
                            }
                        })
                        .subscribeOn(Schedulers.io())
        );

        authUser.addSource(source, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> authResource) {
                authUser.setValue(authResource);
                authUser.removeSource(source);
            }
        });
    }

    public LiveData<AuthResource<User>> observeUser() {
        return authUser;
    }
}
