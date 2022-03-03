# Gitlet Design Document

**Name**: procrastin

## Classes and Data Structures

### MAIN
Takes and verifies user input
#### Fields
1. None?

### COMMIT
Creates a commit object with metadata + hashmap containing files tracked by that commit
#### Fields
1. message: String
2. timestamp: String 
3. parent ID: String
4. filesInCommit: HashMap<filename, blob id>


### COMMIT TREE
#### Fields
1. commitHistory: TreeMap <hashcode, Commit object>
2. Field 2


### REPOSITORY
#### Fields
1. CWD: directory
2. gitlet repository: directory
3. HEAD: file containing path to current commit
4. refs: subdirectory containing branch files (each file contains current commit id)

### BLOB
Creates a Blob object from file content and gets its hashcode 
#### Fields
1. filename
2. hashcode


### STAGE
#### Fields
1. additionStage: HashMap<filename, hashcode>: staging area for addition
2. removalStage: HashMap<filename, hashcode>: staging area for removal
3. modifiedTrackedFiles: HashMap<filename, hashcode>: tracked files that were modified but not staged for commit
4. trackedFiles: HashMap<filename, hashcode>: all files that have ever been committed
5. untrackedFiles: HashMap<filename, hashcode>: filed in CWD that aren't staged or committed
6. index: serialized file containing all info about the staging area






## Algorithms
### MAIN
1. switch/case blocks: call appropriate methods
2. validateNumArguments(): check for number of args (see capers.Main)

### COMMIT
1. getters (get parent ID, date, message, filesInCommit...)
2. saveCommit(): creates new persistent file of commit object with hash code as name (similar to saveDog() in capers)
3. returnCommit(): returns deserialized commit object 
4. getHash(): returns hash code of any commit object
5. 

### COMMIT TREE
1. find(): prints out ids of all commits with given message 
2. log(): prints commit info history
3. globalLog():
4. checkout() [first checkout]
5. checkout() [second checkout]
6. 


### REPOSITORY
1. init(): creates new repo
2. commit(): calls saveCommit()
3. branch(): creates new branch file (same HEAD commit pointer)
4. rm-branch(): deletes the file(commit pointer) with the given name
5. checkout() [third checkout]


### BLOB
1. getHash(): returns hash code of any commit object

### STAGE
1. add()
2. rm()
3. status()
4. saveStage(): serializes the index file after it's been modified (helper)
5. returnStage(): returns deserialized index file into a stage object (helper)

## Persistence

