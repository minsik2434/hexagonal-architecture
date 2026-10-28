package tobyspring.splearn.domain.instructor;

import org.junit.jupiter.api.Test;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InstructorTest {

    @Test
    void apply(){
        Member member = MemberFixture.createActiveMember();

        Instructor instructor = Instructor.apply(member);

        assertThat(instructor.getMember()).isEqualTo(member);
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);
    }

    @Test
    void applyFailed(){
        Member member = MemberFixture.createMember();
        
        assertThatThrownBy(() -> Instructor.apply(member))
            .isInstanceOf(IllegalStateException.class);
    }
    
    @Test
    void approve(){
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        instructor.approve();

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void approveFailed(){
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);
        instructor.approve();

        assertThatThrownBy(instructor::approve)
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void reject(){
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        instructor.reject();

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.REJECTED);
    }

    @Test
    void rejectFailed(){
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);
        instructor.reject();
        
        assertThatThrownBy(instructor::reject)
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isActive(){
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        assertThat(instructor.isActive()).isFalse();

        instructor.approve();
        assertThat(instructor.isActive()).isTrue();
    }
    
    @Test
    void ensureActive(){
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        assertThatThrownBy(instructor::ensureActive)
            .isInstanceOf(IllegalStateException.class);

        instructor.approve();

        instructor.ensureActive();
    }

}