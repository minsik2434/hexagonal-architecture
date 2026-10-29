package tobyspring.splearn.domain.member;

import org.instancio.Instancio;
import org.instancio.Select;
import org.jspecify.annotations.NonNull;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {
    public static MemberRegisterRequest createMemberRegisterRequest(String email) {
        return Instancio.of(MemberRegisterRequest.class)
                .set(Select.field(MemberRegisterRequest::email), email)
                .create();
    }

    public static Member createMember(){
        return Member.register(createMemberRegisterRequest(), createPasswordEncoder());
    }

    public static @NonNull MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest(Instancio.gen().net().email().get());
    }

    public static Member createMember(Long id) {
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    public static Member createActiveMember() {
        Member member = createMember();
        member.activate();
        return member;
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
