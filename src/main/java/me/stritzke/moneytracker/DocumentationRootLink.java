package me.stritzke.moneytracker;

import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class DocumentationRootLink implements RepresentationModelProcessor<RepositoryLinksResource> {
  @Override
  public RepositoryLinksResource process(RepositoryLinksResource resource) {
    resource.add(Link.of("/doc/api-guide.html").withRel("doc"));
    return resource;
  }
}
