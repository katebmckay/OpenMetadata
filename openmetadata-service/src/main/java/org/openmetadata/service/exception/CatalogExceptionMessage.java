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

package org.openmetadata.service.exception;

import java.util.List;
import java.util.UUID;
import org.openmetadata.schema.api.teams.CreateTeam.TeamType;
import org.openmetadata.schema.entity.teams.Team;
import org.openmetadata.schema.type.MetadataOperation;

public final class CatalogExceptionMessage {
  public static final String EMAIL_SENDING_ISSUE =
      "There is some issue in sending the Mail. Please contact your administrator.";
  public static final String PASSWORD_INVALID_FORMAT =
      "Password must be of minimum 8 characters, with one special, one Upper, one lower case character, and one Digit.";
  public static final String MAX_FAILED_LOGIN_ATTEMPT = "Failed Login Attempts Exceeded. Please try after some time.";
  public static final String INVALID_USERNAME_PASSWORD = "You have entered an invalid username or password.";
  public static final String ENTITY_ALREADY_EXISTS = "Entity already exists";
  public static final String FERNET_KEY_NULL = "Fernet key is null";
  public static final String FIELD_NOT_TOKENIZED = "Field is not tokenized";
  public static final String FIELD_ALREADY_TOKENIZED = "Field is already tokenized";
  public static final String INVALID_ENTITY_LINK = "Entity link must have both {arrayFieldName} and {arrayFieldValue}";
  public static final String EMPTY_POLICIES_IN_ROLE = "At least one policy is required in a role";
  public static final String EMPTY_RULES_IN_POLICY = "At least one rule is required in a policy";
  public static final String INVALID_GROUP_TEAM_UPDATE = "Team of type Group cannot be updated";
  public static final String INVALID_GROUP_TEAM_CHILDREN_UPDATE =
      "A team with children cannot be updated to type Group";
  public static final String ANNOUNCEMENT_OVERLAP =
      "There is already an announcement scheduled that overlaps with the given start time and end time";
  public static final String ANNOUNCEMENT_INVALID_START_TIME =
      "Announcement start time must be earlier than the end time";
  public static final String UNEXPECTED_PARENT = "Team of type Organization can't have a parent team";
  public static final String DELETE_ORGANIZATION = "Organization team type can't be deleted";
  public static final String CREATE_ORGANIZATION =
      "Only one Organization is allowed. New Organization type can't be created";
  public static final String CREATE_GROUP =
      "Team of type Group can't have children of type team. Only users are allowed as part of the team";
  public static final String TEAM_HIERARCHY = "Unexpected error occurred while building the teams hierarchy";

  private CatalogExceptionMessage() {}

  public static String entityNotFound(String entityType, String id) {
    return String.format("%s instance for %s not found", entityType, id);
  }

  public static String entityNotFound(String entityType, UUID id) {
    return entityNotFound(entityType, id.toString());
  }

  public static String entitiesNotFound(String entityType) {
    return String.format("%s instances not found", entityType);
  }

  public static String readOnlyAttribute(String entityType, String attribute) {
    return String.format("%s attribute %s can't be modified", entityType, attribute);
  }

  public static String invalidName(String name) {
    return String.format("Invalid name %s", name);
  }

  public static String invalidField(String field) {
    return String.format("Invalid field name %s", field);
  }

  public static String entityTypeNotFound(String entityType) {
    return String.format("Entity type %s not found", entityType);
  }

  public static String resourceTypeNotFound(String resourceType) {
    return String.format("Resource type %s not found", resourceType);
  }

  public static String entityTypeNotSupported(String entityType) {
    return String.format("Entity type %s not supported", entityType);
  }

  public static String deletedUser(UUID id) {
    return String.format("User %s is deleted", id);
  }

  public static String userAlreadyPartOfTeam(String userName, String teamName) {
    return String.format("User '%s' is already part of the team '%s'", userName, teamName);
  }

  public static String invalidColumnFQN(String fqn) {
    return String.format("Invalid fully qualified column name %s", fqn);
  }

  public static String entityVersionNotFound(String entityType, UUID id, Double version) {
    return String.format("%s instance for %s and version %s not found", entityType, id, version);
  }

  public static String invalidServiceEntity(String serviceType, String entityType, String expected) {
    return String.format("Invalid service type `%s` for %s. Expected %s.", serviceType, entityType, expected);
  }

  public static String glossaryTermMismatch(String parentId, String glossaryId) {
    return String.format(
        "Invalid queryParameters - glossary term `parent` %s is not in the `glossary` %s", parentId, glossaryId);
  }

  public static String notAdmin(String name) {
    return String.format("Principal: CatalogPrincipal{name='%s'} is not admin", name);
  }

  public static String permissionDenied(
      String user, MetadataOperation operation, String roleName, String policyName, String ruleName) {
    if (roleName != null) {
      return String.format(
          "Principal: CatalogPrincipal{name='%s'} operation %s denied by role %s, policy %s, rule %s",
          user, operation, roleName, policyName, ruleName);
    }
    return String.format(
        "Principal: CatalogPrincipal{name='%s'} operation %s denied policy %s, rule %s",
        user, operation, policyName, ruleName);
  }

  public static String permissionNotAllowed(String user, List<MetadataOperation> operations) {
    return String.format("Principal: CatalogPrincipal{name='%s'} operations %s not allowed", user, operations);
  }

  public static String entityIsNotEmpty(String entityType) {
    return String.format("%s is not empty", entityType);
  }

  public static String invalidEntity(String entity) {
    return String.format("Invalid entity %s", entity);
  }

  public static String unknownCustomField(String fieldName) {
    return String.format("Unknown custom field %s", fieldName);
  }

  public static String jsonValidationError(String fieldName, String validationMessages) {
    return String.format("Custom field %s has invalid JSON %s", fieldName, validationMessages);
  }

  public static String invalidParent(Team parent, String child, TeamType childType) {
    return String.format(
        "Team %s of type %s can't be of parent of team %s of type %s",
        parent.getName(), parent.getTeamType(), child, childType);
  }

  public static String invalidChild(String parent, TeamType parentType, Team child) {
    return String.format(
        "Team %s of type %s can't have child team %s of type %s",
        parent, parentType, child.getName(), child.getTeamType());
  }

  public static String invalidParentCount(int validParentCount, TeamType teamType) {
    return String.format("Team of type %s can have only %s parents", teamType, validParentCount);
  }

  public static String invalidTeamOwner(TeamType teamType) {
    return String.format("Team of type %s can't own entities. Only Team of type Group can own entities.", teamType);
  }

  public static String failedToParse(String message) {
    return String.format("Failed to parse - %s", message);
  }

  public static String failedToEvaluate(String message) {
    return String.format("Failed to evaluate - %s", message);
  }

  public static String systemEntityDeleteNotAllowed(String name, String entityType) {
    return String.format("System entity [%s] of type %s can not be deleted.", name, entityType);
  }
}
