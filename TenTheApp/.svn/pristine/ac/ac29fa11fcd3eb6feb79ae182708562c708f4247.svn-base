package com.nvcomputers.ten.interfaces;


import com.nvcomputers.ten.model.output.EmailVerificationResponse;
import com.nvcomputers.ten.model.output.LoginResponse;
import com.nvcomputers.ten.model.output.OtpEmailVerifyResponse;
import com.nvcomputers.ten.model.output.PhoneOtpVerifyResponse;
import com.nvcomputers.ten.model.output.PhoneVerifyResponse;
import com.nvcomputers.ten.model.output.RegisterUsernameOutput;
import com.nvcomputers.ten.model.output.ResendEmailOtpResponse;
import com.nvcomputers.ten.model.output.ResendPhoneOtpResponse;

import retrofit2.Call;
import retrofit2.Response;

public class ResultCallbacks {

    public interface ResendPhoneOtpCallback {
        void resendPhoneOtpError(Call<ResendPhoneOtpResponse> call, Throwable t);

        void onResendPhoneOtpSuccess(Response<ResendPhoneOtpResponse> response);
    }

    public interface PhoneOtpVerifyCallback {
        void phoneOtpError(Call<PhoneOtpVerifyResponse> call, Throwable t);

        void onPhoneOtpSuccess(Response<PhoneOtpVerifyResponse> response);
    }

    public interface PhoneVerifyCallback {
        void phoneVerifyError(Call<PhoneVerifyResponse> call, Throwable t);

        void onPhoneVerifySuccess(Response<PhoneVerifyResponse> response);
    }

    public interface ResendEmailOtpCallback {
        void resendEmailOtpError(Call<ResendEmailOtpResponse> call, Throwable t);

        void onResendEmailOtpSuccess(Response<ResendEmailOtpResponse> response);
    }

    public interface OtpEmailVerifyCallback {
        void emailOtpError(Call<OtpEmailVerifyResponse> call, Throwable t);

        void onEmailOtpSuccess(Response<OtpEmailVerifyResponse> response);
    }

    public interface EmailVerificationCallback {
        void emailVerifyError(Call<EmailVerificationResponse> call, Throwable t);

        void onEmailVerifySuccess(Response<EmailVerificationResponse> response);
    }

    public interface LoginCallback {
        void loginError(Call<LoginResponse> call, Throwable t);

        void onLoginSuccess(Response<LoginResponse> response);
    }

    public interface VerifyUserNameCallback {
        void usernameError(Call<RegisterUsernameOutput> call, Throwable t);

        void onUserNameSuccess(Response<RegisterUsernameOutput> response);
    }


}
