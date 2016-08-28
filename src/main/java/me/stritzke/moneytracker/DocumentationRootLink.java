package me.stritzke.moneytracker;

import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

@Component
public class DocumentationRootLink implements ResourceProcessor<RepositoryLinksResource> {
  @Override
  public RepositoryLinksResource process(RepositoryLinksResource resource) {
    resource.add(new Link("/doc/api-guide.html").withRel("doc"));
    return resource;
  }
}
