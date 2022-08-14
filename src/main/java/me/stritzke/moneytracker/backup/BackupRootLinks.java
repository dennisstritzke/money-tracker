package me.stritzke.moneytracker.backup;

import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class BackupRootLinks implements RepresentationModelProcessor<RepositoryLinksResource> {
  @Override
  public RepositoryLinksResource process(RepositoryLinksResource resource) {
    resource.add(linkTo(methodOn(BackupEndpoint.class).doBackup()).withRel("backup"));
    resource.add(linkTo(methodOn(BackupEndpoint.class).doRestore(null, true)).withRel("restore"));
    return resource;
  }
}
