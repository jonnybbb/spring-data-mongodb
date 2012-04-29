package org.springframework.data.mongodb.core.mapping.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.AssociationHandler;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.model.BeanWrapper;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

public class CascadingMongoEventListener extends AbstractMongoEventListener {
	@Autowired
	private MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;

	@Autowired
	private MongoConverter mongoConverter;

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public void onBeforeConvert(final Object source) {
		System.out.println("OnBeforeConvert = " + source);

		final MongoPersistentEntity<?> persistentEntity = mappingContext.getPersistentEntity(source.getClass());

		persistentEntity.doWithAssociations(new CascadeSaveAssociationHandler(source));
	}

	@Override
	public void onAfterDelete(Object source) {
		final MongoPersistentEntity<?> persistentEntity = mappingContext.getPersistentEntity(source.getClass());

		persistentEntity.doWithAssociations(new CascadeDeleteAssociationHandler(source));
	}

	private class CascadeSaveAssociationHandler extends AbstractCascadeAssociationHandler {
		private CascadeSaveAssociationHandler(Object source) {
			super(source);
		}

		@Override
		boolean isAppliable(DBRef dbRef) {
			return dbRef.cascadeType().isSave();
		}

		@Override
		void handle(MongoPersistentProperty mongoPersistentProperty) {
			Object referencedObject = getReferencesObject(mongoPersistentProperty);

			final MongoPersistentEntity<?> referencedObjectEntity = mappingContext.getPersistentEntity(referencedObject.getClass());

			if (referencedObjectEntity.getIdProperty() == null) {
				throw new MappingException("Cannot perform cascade save on child object without id set");
			}

			mongoOperations.save(referencedObject);
		}
	}

	private class CascadeDeleteAssociationHandler extends AbstractCascadeAssociationHandler {
		public CascadeDeleteAssociationHandler(Object source) {
			super(source);
		}

		@Override
		boolean isAppliable(DBRef dbRef) {
			return dbRef.cascadeType().isDelete();
		}

		@Override
		void handle(MongoPersistentProperty mongoPersistentProperty) {
			mongoOperations.remove(getReferencesObject(mongoPersistentProperty));
		}
	}

	private abstract class AbstractCascadeAssociationHandler implements AssociationHandler<MongoPersistentProperty> {
		protected Object source;

		protected AbstractCascadeAssociationHandler(Object source) {
			this.source = source;
		}

		public void doWithAssociation(Association<MongoPersistentProperty> mongoPersistentPropertyAssociation) {
			MongoPersistentProperty persistentProperty = mongoPersistentPropertyAssociation.getInverse();

			if (persistentProperty != null) {
				DBRef dbRef = persistentProperty.getDBRef();

				if (isAppliable(dbRef)) {
					handle(persistentProperty);
				}
			}
		}

		protected Object getReferencesObject(MongoPersistentProperty mongoPersistentProperty) {
			ConversionService service = mongoConverter.getConversionService();

			return BeanWrapper.create(source, service).getProperty(mongoPersistentProperty, Object.class, true);
		}

		abstract boolean isAppliable(DBRef dbRef);

		abstract void handle(MongoPersistentProperty mongoPersistentProperty);
	}
}
