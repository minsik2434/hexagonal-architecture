package tobyspring.splearn.application.member.provided;

import jakarta.validation.Valid;
import tobyspring.splearn.domain.member.Member;

public interface MemberAuthenticator {

    Member login(@Valid MemberLoginRequest loginRequest) throws LoginFailedException;
}
