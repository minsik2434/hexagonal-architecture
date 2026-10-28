package tobyspring.splearn.application.member.provided;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;
import tobyspring.splearn.domain.member.MemberRegisterRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MemberAuthenticatorTest {

    @Autowired
    private MemberAuthenticator memberAuthenticator;

    @Autowired
    private MemberRegister memberRegister;

    @Test
    void login(){
        MemberRegisterRequest registerRequest = MemberFixture.createMemberRegisterRequest();
        Member member = memberRegister.register(registerRequest);
        member.activate();

        var loginedMember = memberAuthenticator.login(new MemberLoginRequest(registerRequest.email(), registerRequest.password()));
        assertThat(loginedMember).isEqualTo(member);
    }
    
    @Test
    void loginFailedNotActive(){
        MemberRegisterRequest registerRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(registerRequest);
        
        assertThatThrownBy(() -> memberAuthenticator.login(new MemberLoginRequest(registerRequest.email(), registerRequest.password())))
            .isInstanceOf(LoginFailedException.class);
    }

    @Test
    void loginFailedEmailNotExists(){
        MemberRegisterRequest registerRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(registerRequest);
        
        assertThatThrownBy(() -> memberAuthenticator.login(new MemberLoginRequest("notexist@email.com", registerRequest.password())))
            .isInstanceOf(LoginFailedException.class);
    }
    
    @Test
    void loginFailedWrongPassword(){
        MemberRegisterRequest registerRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(registerRequest);

        assertThatThrownBy(() -> memberAuthenticator.login(new MemberLoginRequest(registerRequest.email(), "wrongPassword")))
                .isInstanceOf(LoginFailedException.class);
    }
}