package edu.icet.mapper;

import edu.icet.model.dto.MemberDTO;
import edu.icet.model.entity.MemberEntity;

public class MemberMapper {

    public MemberDTO toDTO(MemberEntity memberEntity) {
        if (memberEntity == null) {
            return null;
        }
        return new MemberDTO(
                memberEntity.getMemberId(),
                memberEntity.getName(),
                memberEntity.getEmail(),
                memberEntity.getPhone(),
                memberEntity.getAddress()
        );
    }

    public  MemberEntity toEntity(MemberDTO memberDTO) {
        if (memberDTO == null) {
            return null;
        }
        return new MemberEntity(
                memberDTO.getMemberId(),
                memberDTO.getName(),
                memberDTO.getEmail(),
                memberDTO.getPhone(),
                memberDTO.getAddress()
        );
    }
}
