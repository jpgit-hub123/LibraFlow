package edu.icet.repository;

import edu.icet.model.entity.MemberEntity;

public interface AddMemberRepository {

    boolean saveMember(MemberEntity memberEntity);

    boolean updateMember(String memberId, MemberEntity memberEntity);
}
