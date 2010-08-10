package dbnp.studycapturing

import dbnp.user.User

/**
 * Domain class describing the basic entity in the study capture part: the Study class.
 *
 * Revision information:
 * $Rev$
 * $Author$
 * $Date$
 */
class Study extends TemplateEntity {
	static searchable = {
    	[only: ['title', 'Description']]
    }

	User owner   // The owner of the study. A new study is automatically owned by its creator.
	String title        // The title of the study
	String code 		// currently used as the external study ID, e.g. to reference a study in a SAM module
	Date dateCreated
	Date lastUpdated
	Date startDate
    List subjects
	List events
	List samplingEvents
	List eventGroups
	List samples
	List assays

	static hasMany = [
		editors: User,   // Users with read/write access to the study
		readers: User,   // Users with only read access to the study
		subjects: Subject,
		samplingEvents: SamplingEvent,
		events: Event,
		eventGroups: EventGroup,
		samples: Sample,
		assays: Assay,
		persons: StudyPerson,
		publications: Publication
	]

	static constraints = {
		owner(nullable: true, blank: true)
		code(nullable:false, blank:true,unique:true) 
	}

	static mapping = {
		researchQuestion type: 'text'
		description type: 'text'
		autoTimestamp true
	}

	// The external study ID is currently defined as the code of the study.
	// It is used from within dbNP submodules to refer to particular study in this GSCF instance.
	def getExternalStudyId() { code }

	/**
	 * return the domain fields for this domain class
	 * @return List
	 */
	static List<TemplateField> giveDomainFields() { return Study.domainFields }

	static final List<TemplateField> domainFields = [
		new TemplateField(
			name: 'title',
			type: TemplateFieldType.STRING),
		new TemplateField(
			name: 'code',
			type: TemplateFieldType.STRING,
			preferredIdentifier:true,
			comment: 'Fill out the code by which many people will recognize your study'),
		new TemplateField(
			name: 'startDate',
			type: TemplateFieldType.DATE,
			comment: 'Fill out the official start date or date of first action')
	]

	/**
	 * return the title of this study
	 */
	def String toString() {
		return title
	}

	/**
	 * returns all events and sampling events that do not belong to a group
	 */
	def Set<Event> getOrphanEvents() {
		def orphans =	events.findAll { event -> !event.belongsToGroup(eventGroups) } +
						samplingEvents.findAll { event -> !event.belongsToGroup(eventGroups) }

		return orphans
	}

	/**
	 * Return the unique Subject templates that are used in this study
	 */
	def Set<Template> giveSubjectTemplates() {
		TemplateEntity.giveTemplates(subjects)
	}

	/**
	 * Return all subjects for a specific template
	 * @param Template
	 * @return ArrayList
	 */
	def ArrayList<Subject> giveSubjectsForTemplate(Template template) {
		subjects.findAll { it.template.equals(template) }
	}

	/**
	 * Return the unique Event and SamplingEvent templates that are used in this study
	 */
	Set<Template> giveAllEventTemplates() {
		// For some reason, giveAllEventTemplates() + giveAllSamplingEventTemplates()
		// gives trouble when asking .size() to the result
		// So we also use giveTemplates here
		TemplateEntity.giveTemplates( ((events) ? events : []) + ((samplingEvents) ? samplingEvents : []) )
	}


	/**
	 * Return all events and samplingEvenets for a specific template
	 * @param Template
	 * @return ArrayList
	 */
	def ArrayList giveEventsForTemplate(Template template) {
		def events = events.findAll { it.template.equals(template) }
		def samplingEvents = samplingEvents.findAll { it.template.equals(template) }

		return (events) ? events : samplingEvents
	}

	/**
	 * Return the unique Event templates that are used in this study
	 */
	Set<Template> giveEventTemplates() {
		TemplateEntity.giveTemplates(events)
	}

	/**
	 * Return the unique SamplingEvent templates that are used in this study
	 */
	Set<Template> giveSamplingEventTemplates() {
		TemplateEntity.giveTemplates(samplingEvents)
	}

	/**
	 * Returns the unique Sample templates that are used in the study
	 */
	Set<Template> giveSampleTemplates() {
		TemplateEntity.giveTemplates(samples)
	}

	/**
	 * Return all samples for a specific template
	 * @param Template
	 * @return ArrayList
	 */
	def ArrayList<Subject> giveSamplesForTemplate(Template template) {
		samples.findAll { it.template.equals(template) }
	}

	/**
	 * Returns the template of the study
	 */
	Template giveStudyTemplate() {
		return this.template
	}


	/**
	 * Delete a specific subject from this study, including all its relations
	 * @param subject The subject to be deleted
	 * @return A String which contains a (user-readable) message describing the changes to the database
	 */
	String deleteSubject(Subject subject) {
		String msg = "Subject ${subject.name} was deleted"

		// Delete the subject from the event groups it was referenced in
		this.eventGroups.each {
			if (it.subjects.contains(subject)) {
				it.removeFromSubjects(subject)
				msg += ", deleted from event group '${it.name}'"
			}
		}

		// Delete the samples that have this subject as parent
		this.samples.findAll { it.parentSubject.equals(subject) }.each {
			// This should remove the sample itself too, because of the cascading belongsTo relation
			this.removeFromSamples(it)
			// But apparently it needs an explicit delete() too
			it.delete()
			msg += ", sample '${it.name}' was deleted"
		}

		// This should remove the subject itself too, because of the cascading belongsTo relation
		this.removeFromSubjects(subject)
		// But apparently it needs an explicit delete() too
		subject.delete()

		return msg
	}

	/**
	 * Delete an event from the study, including all its relations
	 * @param Event
	 * @return String
	 */
	String deleteEvent(Event event) {
		String msg = "Event ${event} was deleted"

		// remove event from the study
		this.removeFromEvents(event)

		// remove event from eventGroups
		this.eventGroups.each() { eventGroup ->
			eventGroup.removeFromEvents(event)
		}

		return msg
	}

	/**
	 * Delete a samplingEvent from the study, including all its relations
	 * @param SamplingEvent
	 * @return String
	 */
	String deleteSamplingEvent(SamplingEvent samplingEvent) {
		String msg = "SamplingEvent ${samplingEvent} was deleted"

		// remove event from eventGroups
		this.eventGroups.each() { eventGroup ->
			eventGroup.removeFromSamplingEvents(samplingEvent)
		}

		// remove event from the study
		this.removeFromSamplingEvents(samplingEvent)

		return msg
	}
	/**
	 * Delete an eventGroup from the study, including all its relations
	 * @param EventGroup
	 * @return String
	 */
	String deleteEventGroup(EventGroup eventGroup) {
		String msg = "EventGroup ${eventGroup} was deleted"

		// remove the eventGroup from the study
		this.removeFromEventGroups(eventGroup)

		return msg
	}
}
