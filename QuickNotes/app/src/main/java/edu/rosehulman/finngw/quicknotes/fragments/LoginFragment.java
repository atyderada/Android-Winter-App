package edu.rosehulman.finngw.quicknotes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import edu.rosehulman.finngw.quicknotes.R;

public class LoginFragment extends Fragment {

    private static final boolean SHOW_EMAIL_PASSWORD = false;
    private EditText mPasswordView;
    private EditText mEmailView;
    private View mLoginForm;
    private View mProgressSpinner;
    private boolean mLoggingIn;
    private OnLoginListener mOnLoginListener;
    private DatabaseReference mFirebaseRef;
    private FirebaseAuth mAuth;
    private View mGoogleLoginButton;
    private View mEmailLoginButton;
    private View mRosefireLoginButton;

    public LoginFragment() {
        // Required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoggingIn = false;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_login, container, false);

        mEmailView = (EditText) rootView.findViewById(R.id.email_edit_text);
        mPasswordView = (EditText) rootView.findViewById(R.id.password_edit_text);
        mEmailLoginButton = (ImageButton)rootView.findViewById(R.id.email_login_button);
        mGoogleLoginButton = (ImageButton)rootView.findViewById(R.id.gmail_login_button);
        mRosefireLoginButton = (ImageButton) rootView.findViewById(R.id.rosefire_login_button);

        if (SHOW_EMAIL_PASSWORD) {
            // Feel free to customize defaults here to speed your testing
            mEmailView.setText("a@b.com");
            mPasswordView.setText("aaaaaa");
        } else {
            mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_NEXT) {
                        mGoogleLoginButton.setVisibility(View.GONE);
                        mRosefireLoginButton.setVisibility(View.GONE);
                        mPasswordView.requestFocus();
                        return true;
                    }
                    return false;
                }
            });
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_NULL) {
                        login();
                        return true;
                    }
                    return false;
                }
            });
            mEmailLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    login();
                }
            });
            mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginWithGoogle();
                }
            });
            mRosefireLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginWithRosefire();
                }
            });

        }
        return rootView;
    }

    private void loginWithGoogle() {
        if(mLoggingIn) {
            return;
        }
        mEmailView.setError(null);
        mPasswordView.setError(null);

        mLoggingIn = true;
        mOnLoginListener.onGoogleLogin();
        hideKeyboard();
    }

    private void loginWithRosefire() {
        if(mLoggingIn) {
            return;
        }
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mLoggingIn = true;

        mOnLoginListener.onRosefireLogin();
        hideKeyboard();
    }
    public void login() {
        if (mLoggingIn) {
            return;
        }

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancelLogin = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.invalid_password));
            focusView = mPasswordView;
            cancelLogin = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.field_required));
            focusView = mEmailView;
            cancelLogin = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.invalid_email));
            focusView = mEmailView;
            cancelLogin = true;
        }

        if (cancelLogin) {
            // error in login
            focusView.requestFocus();
        } else {
            // show progress spinner, and start background task to login
            mLoggingIn = true;
            mOnLoginListener.onLogin(email, password);
            hideKeyboard();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
        mGoogleLoginButton.setVisibility(View.VISIBLE);
        mRosefireLoginButton.setVisibility(View.VISIBLE);
    }

    public void onLoginError(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.login_error))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();

        mLoggingIn = false;
    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 0;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mOnLoginListener = (OnLoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnLoginListener {
        void onLogin(String email, String password);
        void onGoogleLogin();
        void onRosefireLogin();
    }

 }