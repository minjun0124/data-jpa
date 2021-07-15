package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /**
     * Domain class converter
     *  - 복잡한 경우에는 지양한다.
     */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    /**
     * data-jpa web extension - paging
     * request parameter
     *    ex)  /members?page=0&size=3&sort=id,desc&sort=username,desc
     */
    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    /**
     * DTO 변환
     */
    @GetMapping("/members")
    public Page<MemberDto> listDto(Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberDto::new);
    }

    /**
     * paging 개별설정
     */
//    @GetMapping("/members")
//    public Page<Member> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
//        return memberRepository.findAll(pageable);
//    }

    /**
     * insert dummy data
     */
    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }

    /**
     * paging 정보가 둘 이상이면 접두사로 구분
     * ex) /members?member_page=0&order_page=1
     */
//    public String list(
//            @Qualifier("member") Pageable memberPageable,
//            @Qualifier("order") Pageable orderPageable, ...


}