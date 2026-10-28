package tobyspring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.member.Member;
import tobyspring.support.stereotype.ApplicationServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;

@ApplicationServiceTest
record MemberFinderTest(MemberFinder memberFinder, MemberRegister memberRegister, EntityManager em) {

    @Test
    void find(){
        Member member = memberRegister.register(createMemberRegisterRequest());
        em.flush(); em.clear();

        Member found = memberFinder.find(member.getId());
        
        assertThat(member.getId()).isEqualTo(found.getId());
    }
    
    @Test
    void findFail(){
        assertThatThrownBy(() -> memberFinder.find(999L))
            .isInstanceOf(IllegalArgumentException.class);
    }
}