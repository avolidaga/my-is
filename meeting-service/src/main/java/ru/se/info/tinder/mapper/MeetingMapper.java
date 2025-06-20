package ru.se.info.tinder.mapper;

import ru.se.info.tinder.dto.CreateMeetingDto;
import ru.se.info.tinder.dto.UpdateMeetingDto;
import ru.se.info.tinder.dto.MeetingDto;
import ru.se.info.tinder.model.FabricTexture;
import ru.se.info.tinder.model.Meeting;
import ru.se.info.tinder.model.User;

import java.time.LocalDateTime;

public class MeetingMapper {
    public static Meeting toEntitySpacesuitData(CreateMeetingDto dto, FabricTexture fabricTexture) {
        return Meeting.builder()
                .head(dto.getHead())
                .chest(dto.getChest())
                .waist(dto.getWaist())
                .hips(dto.getHips())
                .footSize(dto.getFootSize())
                .height(dto.getHeight())
                .fabricTexture(fabricTexture)
                .ownerUser(User.builder().id(dto.getId()).build())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Meeting toEntitySpacesuitData(UpdateMeetingDto dto, Meeting meeting, FabricTexture fabricTexture) {
        return Meeting.builder()
                .id(meeting.getId())
                .head(dto.getHead())
                .chest(dto.getChest())
                .waist(dto.getWaist())
                .hips(dto.getHips())
                .footSize(dto.getFootSize())
                .height(dto.getHeight())
                .fabricTexture(fabricTexture)
                .createdAt(meeting.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .ownerUser(meeting.getOwnerUser())
                .build();
    }

    public static MeetingDto toSpacesuitDataDto(Meeting entity) {
        return MeetingDto.builder()
                .head(entity.getHead())
                .chest(entity.getChest())
                .waist(entity.getWaist())
                .hips(entity.getHips())
                .footSize(entity.getFootSize())
                .height(entity.getHeight())
                .fabricTextureDto(FabricTextureMapper.toDtoFabricTexture(entity.getFabricTexture()))
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
