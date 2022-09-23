package edu.bbte.idde.vgim1978.spring.controller.mapper;

import edu.bbte.idde.vgim1978.spring.controller.dto.incoming.MenuCreationDto;
import edu.bbte.idde.vgim1978.spring.controller.dto.outgoing.MenuResponseDto;
import edu.bbte.idde.vgim1978.spring.model.Menu;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import java.util.Collection;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    Menu dtoToMenu(MenuCreationDto menuCreationDto);

    MenuResponseDto menuToResponseDto(Menu menu);

    @IterableMapping(elementTargetType = MenuResponseDto.class)
    Collection<MenuResponseDto> menuListToResponseDtoList(Collection<Menu> menus);

}
