package edu.icet.repository.impl;

import edu.icet.db.DBConnection;
import edu.icet.model.entity.MemberEntity;
import edu.icet.repository.AddMemberRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AddMemberRepositoryImpl implements AddMemberRepository {

    private final Session session = DBConnection.getSession();

    public boolean saveMember(MemberEntity memberEntity) {
        try {
            Transaction transaction = session.beginTransaction();
            session.persist(memberEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateMember(String memberId, MemberEntity memberEntity) {
        Transaction transaction = session.beginTransaction();
        MemberEntity existingMemberEntity = session.find(MemberEntity.class, memberId);
        if (existingMemberEntity == null) {
            return false;
        }
        existingMemberEntity.setName(memberEntity.getName());
        existingMemberEntity.setEmail(memberEntity.getEmail());
        existingMemberEntity.setPhone(memberEntity.getPhone());
        existingMemberEntity.setAddress(memberEntity.getAddress());
        transaction.commit();
        return true;
    }
}
