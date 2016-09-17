package me.stritzke.moneytracker.backup;

import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
class BackupRootLinks implements ResourceProcessor<RepositoryLinksResource> {
  @Override
  public RepositoryLinksResource process(RepositoryLinksResource resource) {
    resource.add(linkTo(methodOn(BackupEndpoint.class).doBackup()).withRel("backup"));
    resource.add(linkTo(methodOn(BackupEndpoint.class).doRestore(null, true)).withRel("restore"));
    return resource;
  }
}
