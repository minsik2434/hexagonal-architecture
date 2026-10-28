package tobyspring.splearn.application.instructor.provided;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.domain.instructor.Instructor;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(SplearnTestConfiguration.class)
@RequiredArgsConstructor
class InstructorFinderTest {
    final InstructorFinder instructorFinder;
    final InstructorApplication instructorApplication;
    final MemberRegister memberRegister;
    
    @Test
    void findByMember(){
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        member = memberRegister.activate(member.getId());

        Instructor instructor = instructorApplication.apply(new InstructorApplyRequest(member.getId()));

        Instructor found = instructorFinder.findByMember(member.getId()).orElseThrow();
        
        assertThat(instructor).isEqualTo(found);

        assertThat(instructorFinder.findByMember(Long.MAX_VALUE).isPresent()).isFalse();
    }

}