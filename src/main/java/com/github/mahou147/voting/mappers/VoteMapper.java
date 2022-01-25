package com.github.mahou147.voting.mappers;

import com.github.mahou147.voting.model.Vote;
import com.github.mahou147.voting.to.VoteTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VoteMapper {

    VoteMapper INSTANCE = Mappers.getMapper(VoteMapper.class);

    @Mapping(source = "restaurant.id", target = "restaurantId")
    VoteTo toVoteTo(Vote v);
}
