package com.knackitsolutions.crm.imaginepenguins.dbservice.assembler;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.WebDashboardDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class WebDashboardModelAssembler implements RepresentationModelAssembler<WebDashboardDTO, EntityModel<WebDashboardDTO>> {
    @Override
    public CollectionModel<EntityModel<WebDashboardDTO>> toCollectionModel(Iterable<? extends WebDashboardDTO> entities) {
        return null;
    }

    @Override
    public EntityModel<WebDashboardDTO> toModel(WebDashboardDTO entity) {
        return null;
    }
}
