package tobyspring.splearn.domain;

import org.jspecify.annotations.NonNull;

public class MemberFixture {
    public static MemberRegisterRequest createMemberRegisterRequest(String email) {
        return new MemberRegisterRequest(email, "minsik", "longsecret");
    }

    public static @NonNull MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest("la9814@naver.com");
    }

    public static @NonNull PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }
}
