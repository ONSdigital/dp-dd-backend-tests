package com.ons.gov.uk.core.model;

import javax.persistence.*;
import java.util.List;

/**
 * Collects metadata for a dataset/resource.
 */
@Embeddable
public class Metadata {
	@Column
	private String description;

	@Embedded
	private Contact contact;

	@Column(name = "release_date")
	private String releaseDate;

	@Column(name = "next_release")
	private String nextRelease;

	/**
	 * Whether this is an official National Statistics dataset or not.
	 *
	 * @see <a href="https://www.statisticsauthority.gov.uk/national-statistician/types-of-official-statistics/">Types of official statistics</a>
	 */
	@Column
	private boolean nationalStatistics;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "associated_publications",
			joinColumns = @JoinColumn(name = "dimensional_data_set_id", columnDefinition = "uuid"))
	@Column(name = "associated_publication_url")
	private List <String> associatedPublications;

	@Column
	private String methodology;

	@Column
	private String termsAndConditions;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getNextRelease() {
		return nextRelease;
	}

	public void setNextRelease(String nextRelease) {
		this.nextRelease = nextRelease;
	}

	public boolean isNationalStatistics() {
		return nationalStatistics;
	}

	public void setNationalStatistics(boolean nationalStatistics) {
		this.nationalStatistics = nationalStatistics;
	}

	public List <String> getAssociatedPublications() {
		return associatedPublications;
	}

	public void setAssociatedPublications(List <String> associatedPublications) {
		this.associatedPublications = associatedPublications;
	}

	public String getMethodology() {
		return methodology;
	}

	public void setMethodology(String methodology) {
		this.methodology = methodology;
	}

	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

	@Override
	public String toString() {
		return "Metadata{" +
				"description='" + description + '\'' +
				", contact=" + contact +
				", releaseDate='" + releaseDate + '\'' +
				", nextRelease='" + nextRelease + '\'' +
				", nationalStatistics=" + nationalStatistics +
				", associatedPublications=" + associatedPublications +
				", methodology='" + methodology + '\'' +
				", termsAndConditions='" + termsAndConditions + '\'' +
				'}';
	}
}
