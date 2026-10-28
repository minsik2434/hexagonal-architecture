package tobyspring.splearn.application.instructor.provided;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.application.instructor.required.InstructorRepository;
import tobyspring.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.domain.instructor.Instructor;
import tobyspring.splearn.domain.instructor.InstructorFixture;
import tobyspring.splearn.domain.instructor.InstructorStatus;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(SplearnTestConfiguration.class)
@RequiredArgsConstructor
class InstructorApplicationTest {
    final InstructorApplication instructorApplication;
    final InstructorRepository instructorRepository;
    final MemberRepository memberRepository;
    
    @Test
    void apply(){
        Member member = MemberFixture.createActiveMember();
        memberRepository.save(member);

        Instructor instructor = instructorApplication.apply(InstructorFixture.createApplyRequest(member));
        assertThat(instructor.getId()).isNotNull();
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);

        instructorRepository.findById(instructor.getId()).get();
    }

    @Test
    void duplicateApply(){
        Member member = MemberFixture.createActiveMember();
        memberRepository.save(member);

        instructorApplication.apply(new InstructorApplyRequest(member.getId()));
        assertThatThrownBy(() -> instructorApplication.apply(InstructorFixture.createApplyRequest(member)))
            .isInstanceOf(DuplicateInstructorApplicationException.class);
    }

    @Test
    void approve(){
        Instructor instructor = instructorApplication.approve(preparePendingInstructor().getId());
        
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void reject(){
        Instructor instructor = instructorApplication.reject(preparePendingInstructor().getId());

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.REJECTED);
    }

    private Instructor preparePendingInstructor() {
        Member member = MemberFixture.createActiveMember();
        memberRepository.save(member);
        return instructorApplication.apply(InstructorFixture.createApplyRequest(member));
    }
}