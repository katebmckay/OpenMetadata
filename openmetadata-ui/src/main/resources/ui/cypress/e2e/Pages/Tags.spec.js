/*
 *  Copyright 2021 Collate
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import {
    addNewTagToEntity,
    descriptionBox,
    interceptURL,
    login,
    verifyResponseStatusCode
} from '../../common/common';
import {
    LOGIN,
    NEW_TAG,
    NEW_TAG_CATEGORY,
    SEARCH_ENTITY_TABLE
} from '../../constants/constants';

describe('Tags page should work', () => {
  beforeEach(() => {
    login(LOGIN.username, LOGIN.password);
    cy.goToHomePage();
    interceptURL('GET', '/api/v1/tags*', 'getTags');
    cy.get('[data-testid="appbar-item-tags"]')
      .should('be.visible')
      .click({ force: true });
    verifyResponseStatusCode('@getTags', 200);
  });

  it('Required Details should be available', () => {
    cy.get('[data-testid="add-category"]').should('be.visible');
    cy.get('[data-testid="add-new-tag-button"]').should('be.visible');
    cy.get('[data-testid="delete-tag-category-button"]').should('be.visible');
    cy.get('[data-testid="description"]').should('be.visible');
    cy.get('[data-testid="table"]').should('be.visible');

    cy.get('.ant-table-thead > tr > .ant-table-cell')
      .eq(0)
      .contains('Name')
      .should('be.visible');
    cy.get('.ant-table-thead > tr > .ant-table-cell')
      .eq(1)
      .contains('Description')
      .should('be.visible');
    cy.get('.ant-table-thead > tr > .ant-table-cell')
      .eq(2)
      .contains('Actions')
      .should('be.visible');

    cy.get('.activeCategory > .tag-category')
      .should('be.visible')
      .invoke('text')
      .then((text) => {
        cy.get('.activeCategory > .tag-category')
          .should('be.visible')
          .invoke('text')
          .then((heading) => {
            expect(text).to.equal(heading);
          });
      });
  });

  it('Add new tag category flow should work properly', () => {
    cy.get('[data-testid="add-category"]').should('be.visible').click();
    cy.get('.tw-modal-container').should('be.visible');
    cy.get('[data-testid="name"]')
      .should('be.visible')
      .type(NEW_TAG_CATEGORY.name);
    cy.get(descriptionBox)
      .should('be.visible')
      .type(NEW_TAG_CATEGORY.description);

    cy.get('[data-testid="saveButton"]')
      .scrollIntoView()
      .should('be.visible')
      .click();
    cy.get('.tw-modal-container').should('not.exist');
    cy.get('[data-testid="category-name"]')
      .should('be.visible')
      .invoke('text')
      .then((text) => {
        expect(text).to.equal(NEW_TAG_CATEGORY.name);
      });
  });

  it('Add new tag flow shoud work properly', () => {
    cy.get('[data-testid="side-panel-category"]')
      .contains(NEW_TAG_CATEGORY.name)
      .should('be.visible')
      .as('newCategory');

    cy.get('@newCategory')
      .click()
      .parent()
      .should('have.class', 'activeCategory');
    cy.get('[data-testid="add-new-tag-button"]').should('be.visible').click();
    cy.get('.tw-modal-container').should('be.visible');
    cy.get('[data-testid="name"]').should('be.visible').type(NEW_TAG.name);
    cy.get(descriptionBox).should('be.visible').type(NEW_TAG.description);

    interceptURL('GET', '/api/v1/tags/*', 'createTag');
    cy.get('[data-testid="saveButton"]').should('be.visible').click();

    verifyResponseStatusCode('@createTag', 200);

    cy.get('tbody').should('contain', NEW_TAG.name);
  });

  it('Use newly created tag to any entity should work', () => {
    const entity = SEARCH_ENTITY_TABLE.table_2;
    addNewTagToEntity(entity, `${NEW_TAG_CATEGORY.name}.${NEW_TAG.name}`);
  });

  it('Check Usage of tag and it should redirect to explore page with tags filter', () => {
    cy.get('[data-testid="side-panel-category"]')
      .contains(NEW_TAG_CATEGORY.name)
      .should('be.visible')
      .as('newCategory');
    cy.get('@newCategory')
      .click()
      .parent()
      .should('have.class', 'activeCategory');

    cy.get('[data-testid="usage-count"]').should('be.visible').as('count');
    cy.get('@count')
      .invoke('text')
      .then((text) => {
        expect(text).to.equal('2');
      });

    cy.get('@count').click();

    cy.wait(500);
    cy.get('[data-testid="table-data-card"]')
      .first()
      .contains(`#${NEW_TAG_CATEGORY.name}.${NEW_TAG.name}`)
      .should('be.visible');

    cy.get('[data-testid="filter-container-TestCategory.test"]')
      .should('be.visible')
      .find('[data-testid="checkbox"]')
      .should('be.visible')
      .should('be.checked');
  });

  it('Delete tag flow should work properly', () => {
    cy.get('[data-testid="side-panel-category"]')
      .contains(NEW_TAG_CATEGORY.name)
      .should('be.visible')
      .as('newCategory');

    cy.get('@newCategory')
      .click()
      .parent()
      .should('have.class', 'activeCategory');
    cy.get('.ant-table-row > :nth-child(1)')
      .contains(NEW_TAG.name)
      .should('be.visible');

    cy.get('[data-testid="delete-tag"]').should('be.visible').click();
    cy.get('.tw-modal-container').should('be.visible');
    cy.get('[data-testid="body-text"]')
      .contains(`Are you sure you want to delete the tag "${NEW_TAG.name}"?`)
      .should('be.visible');

    interceptURL(
      'DELETE',
      `/api/v1/tags/${NEW_TAG_CATEGORY.name}/*`,
      'deleteTag'
    );
    cy.get('[data-testid="save-button"]').should('be.visible').click();
    verifyResponseStatusCode('@deleteTag', 200);
    cy.get('.tw-modal-container').should('not.exist');
    cy.get('.ant-table-placeholder').should('be.visible');
  });

  it('Delete Tag flow should work properly', () => {
    cy.get('[data-testid="side-panel-category"]')
      .contains(NEW_TAG_CATEGORY.name)
      .should('be.visible')
      .as('newCategory');

    cy.get('@newCategory')
      .click()
      .parent()
      .should('have.class', 'activeCategory');

    cy.get('[data-testid="delete-tag-category-button"]')
      .should('be.visible')
      .click();
    cy.get('.tw-modal-container').should('be.visible');
    cy.contains(
      `Are you sure you want to delete the tag category "${NEW_TAG_CATEGORY.name}"?`
    ).should('be.visible');

    cy.get('[data-testid="save-button"]').should('be.visible').click();

    cy.get('[data-testid="side-panel-category"]')
      .contains(NEW_TAG_CATEGORY.name)
      .should('not.be.exist');
  });
});
