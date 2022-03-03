# Gitlet Design Document

**Name**: procrastin

## Classes and Data Structures

### Main
Takes and verifies user input
#### Fields
1. None?

### Commit
Creates a commit object with metadata + hashmap containing files tracked by that commit
#### Fields
1. message: String
2. timestamp: String 
3. parent ID: String
4. filesInCommit: HashMap<filename, blob id>


### Commit tree
#### Fields
1. commitHistory: TreeMap <hashcode, Commit object>
2. Field 2


### Repository
#### Fields
1. CWD: directory
2. gitlet repository: directory
3. HEAD: file containing path to current commit
4. refs: subdirectory containing branch files (each file contains current commit id)

### Blob
#### Fields
1. filename
2. hashcode



### Stage
#### Fields
1. additionStage: HashMap<filename, hashcode>
2. removalStage: HashMap<filename, hashcode>
3. modifiedTrackedStage: HashMap<filename, hashcode>
4. untrackedFiles: HashMap<filename, hashcode>

## Algorithms
### Main
1. switch/case blocks: call appropriate methods
2. validateNumArguments(): check for number of args (see capers.Main)

### Commit
1. getters (get parent ID, date, message, filesInCommit...)
2. storeCommit(): creates new persistent file of commit object with hash code as name (similar to saveDog() in capers)
3. retrieveCommit(): returns deserialized commit object 
4. getHash(): returns hash code of any commit object
5. 

### Commit tree 
1. find(): prints out ids of all commits with given message 
2. log()
3. globaLog()
4. branch()
5. merge()

### Repository 
1. init(): creates new repo
2. commit(): calls saveCommit()

### Blob
1. getHash(): returns hash code of any commit object

### Stage
1. add()
2. rm()
3. status()

## Persistence

