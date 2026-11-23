package edu.icet.repository;

import edu.icet.model.entity.MemberEntity;

import java.util.List;

public interface MembersRepository {

    boolean hasIssueHistory(int memberId);

    boolean deleteMember(String memberId);

    List<MemberEntity> getAllMembers();
}
