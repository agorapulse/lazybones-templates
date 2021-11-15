package $pkg;

import grails.core.ArtefactHandler;
import grails.core.ArtefactInfo;
import grails.core.GrailsApplication;
import grails.core.GrailsClass;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.grails.core.AbstractGrailsApplication;
import org.grails.datastore.mapping.model.MappingContext;
import org.springframework.core.io.Resource;

import javax.inject.Singleton;

@Factory
public class MockGrailsApplicationFactory {

    @Bean
    @Singleton
    public GrailsApplication grailsApplication() {
        return new AbstractGrailsApplication() {

            @Override
            public Class[] getAllClasses() {
                return new Class[0];
            }

            @Override
            public Class[] getAllArtefacts() {
                return new Class[0];
            }

            @Override
            public MappingContext getMappingContext() {
                return null;
            }

            @Override
            public void setMappingContext(MappingContext mappingContext) {

            }

            @Override
            public void refresh() {

            }

            @Override
            public void rebuild() {

            }

            @Override
            public Resource getResourceForClass(Class theClazz) {
                return null;
            }

            @Override
            public boolean isArtefact(Class theClazz) {
                return false;
            }

            @Override
            public boolean isArtefactOfType(String artefactType, Class theClazz) {
                return false;
            }

            @Override
            public boolean isArtefactOfType(String artefactType, String className) {
                return false;
            }

            @Override
            public GrailsClass getArtefact(String artefactType, String name) {
                return null;
            }

            @Override
            public ArtefactHandler getArtefactType(Class theClass) {
                return null;
            }

            @Override
            public ArtefactInfo getArtefactInfo(String artefactType) {
                return null;
            }

            @Override
            public GrailsClass[] getArtefacts(String artefactType) {
                return new GrailsClass[0];
            }

            @Override
            public GrailsClass getArtefactForFeature(String artefactType, Object featureID) {
                return null;
            }

            @Override
            public GrailsClass addArtefact(String artefactType, Class artefactClass) {
                return null;
            }

            @Override
            public GrailsClass addArtefact(String artefactType, GrailsClass artefactGrailsClass) {
                return null;
            }

            @Override
            public void registerArtefactHandler(ArtefactHandler handler) {

            }

            @Override
            public boolean hasArtefactHandler(String type) {
                return false;
            }

            @Override
            public ArtefactHandler[] getArtefactHandlers() {
                return new ArtefactHandler[0];
            }

            @Override
            public void initialise() {

            }

            @Override
            public boolean isInitialised() {
                return false;
            }

            @Override
            public GrailsClass getArtefactByLogicalPropertyName(String type, String logicalName) {
                return null;
            }

            @Override
            public void addArtefact(Class artefact) {

            }

            @Override
            public void addOverridableArtefact(Class artefact) {

            }

            @Override
            public ArtefactHandler getArtefactHandler(String type) {
                return null;
            }
        };
    }

}
