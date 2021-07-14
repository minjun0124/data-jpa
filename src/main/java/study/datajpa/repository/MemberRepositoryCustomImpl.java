package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * MemberRepository 에 사용자 정의 인터페이스를 상속하여 사용하는 방법도 있지만,
 * 이 클래스와 같이 그냥 따로 클래스를 만들어 빼는 방법도 방법이다.
 * 결국 전체 아키텍쳐와 라이프 사이클을 고려하여 Repository 를 어떻게 분리할 것인지
 * 고민을 해보고 상황에 맞게 분리하는 것이 옳다.
 */
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final EntityManager em;
    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}