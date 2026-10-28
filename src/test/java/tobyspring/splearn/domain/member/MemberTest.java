package tobyspring.splearn.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static tobyspring.splearn.domain.member.MemberFixture.createPasswordEncoder;


class MemberTest {
    Member member;

    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        this.passwordEncoder = createPasswordEncoder();
        member = Member.register(createMemberRegisterRequest(), passwordEncoder);
    }
    
    @Test
    void registerMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(member.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void createMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void activate(){
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void activateFail(){
        member.activate();

        assertThatThrownBy(() -> {
            member.activate();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate(){
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void deactivateFail(){
        assertThatThrownBy(() -> {
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword(){
        assertThat(member.verifyPassword("longsecret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("invalid", passwordEncoder)).isFalse();
    }

    @Test
    void changePassword(){
        member.changePassword("verysecret", passwordEncoder);

        assertThat(member.verifyPassword("verysecret", passwordEncoder)).isTrue();
    }

    @Test
    void isActive(){
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }

    @Test
    void invalidEmail(){
        assertThatThrownBy(() ->
                Member.register(createMemberRegisterRequest("invalid"), passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    void updateInfo() {
        member.activate();

        var request = new MemberInfoUpdateRequest("choiminsik", "minsik0001", "자기소개");
        member.updateInfo(request);

        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getDetail().getProfile().address()).isEqualTo(request.profileAddress());
        assertThat(member.getDetail().getIntroduction()).isEqualTo(request.introduction());
    }

    @Test
    void updateInfoFail() {
        assertThatThrownBy(() -> {
            var request = new MemberInfoUpdateRequest("minsik", "minsik001", "자기 소개");
            member.updateInfo(request);
        })
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void url() {
        var profile = new Profile("minsik");

        assertThat(profile.url()).isEqualTo("@minsik");
    }


}