package com.vivido.domain;

import java.sql.Timestamp;

public class MemberVO {
	
    private String memberId;                  // 아이디
    private String memberPw;                  // 비밀번호
    private String memberName;                // 이름
    private String memberNickname;            // 닉네임
    private String memberTel;                 // 전화번호
    private String memberGender;              // 성별
    private String memberBirth;               // 생년월일
    private String memberEmail;               // 이메일
    private Timestamp signupDate;             // 가입일
    private Timestamp approvalDate;           // 승인일
    private String memberProfile;             // 이미지 (프로필 이미지 경로)
    private int permissionId;                 // 권한 ID
    private String mailSubscription;          // 메일수신여부
    private String roadAddress;               // 도로명 주소
    private String jibunAddress;              // 지번 주소
    private String postalCode;                // 우편번호
    private String detailAddress;             // 상세 주소
    private String extraAddress;              // 참고 항목
    private String instNumber;                // 사업자등록번호
    private String approvalStatus;            // 승인상태
	
}
