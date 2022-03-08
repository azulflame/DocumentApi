# Description

This is a small project that I am using to learn Spring Boot.
This REST API allows you to query a set of documents to find relevant documents. All search terms must be present in each returned document.

# Usage

## Requirements

- Java 8+
- Gradle 6.8+

## Running

If you have a document body to import, format it as JSON with the `title` and `text` properties, stored in `corpus.json` in the project root.
To run the project, use the gradle bootRun task.

```
gradle bootRun
```

# Endpoints

## GET

### GET /api/query

Get a list of document ids and titles that match all the words in the query

#### Parameters

| Parameter | Required | Type   | Description                                   |
| --------- | -------- | ------ | --------------------------------------------- |
| `query`   | required | string | The query to search the document database for |

#### Response

```
[
    {
        "id": 21576018,
        "title": "List of Friday the 13th characters"
    },
    {
        "id": 21576903,
        "title": "Matt Bomer"
    }
]
```

### GET /api/document/{id}

Get a document with the ID specified in the path

#### Response

```
{
    "id": 19331052,
    "title": "document title here",
    "text": "document text here"
}
```

## POST

### POST /api/document

Submit a document to the database. It will return the saved document.

#### Parameters

| Parameter | Required | Type   | Description               |
| --------- | -------- | ------ | ------------------------- |
| `title`   | required | string | The title of the document |
| `text`    | required | string | The text of the document  |

#### Response

```

```
