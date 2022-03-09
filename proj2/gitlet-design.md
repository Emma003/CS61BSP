# Gitlet Design Document

**Name**: procrastin

## Classes and Data Structures

### MAIN
Takes and verifies user input
#### Fields
1. None?

### COMMIT (Serializable)
Creates a commit object with metadata + treemap containing files tracked by that commit
#### Fields
1. message: String
2. timestamp: String 
3. parent ID: String
4. isMergeCommit: boolean
5. filesInCommit: TreeMap<filename, blob id>

### COMMIT TREE
Handles functions related to the commit history including commit, merge, checkout, reset, log
#### Fields
2. splitPoint: String 

### REPOSITORY
Handles operations related to the status of the repo and the CWD
#### Fields
1. CWD: directory
2. GITLET_DIR: directory
3. HEAD: file containing path to current branch
4. MASTER: file containing id of current commit 
4. BRANCHES: subdirectory containing branch files (each file contains current commit id)
5. COMMITS: subdirectory containing blobs and commits 
6. BLOBS: : subdirectory containing blobs

### BLOB (Serializable)
Creates a Blob object from file content and gets its hashcode
#### Fields
1. filename: String
2. hashcode: String
3. contents: String

### STAGE (Serializable)
Handles funcitons related to the staging area (add, rm, status...)
#### Fields
1. additionStage: HashMap<filename, hashcode>: staging area for addition
2. removalStage: HashMap<filename, hashcode>: staging area for removal
3. modifiedTrackedFiles: HashMap<filename, hashcode>: tracked files that were modified but not staged for commit
4. trackedFiles: HashMap<filename, hashcode>: all files that have ever been committed
5. untrackedFiles: ArrayList<String>: filed in CWD that aren't staged or committed
6. INDEX: serialized file containing all info about the staging area



## Algorithms


### MAIN
1. switch/case blocks: call appropriate methods
2. validateNumArgs(): check for number of args (see capers.Main)

### COMMIT
1. getters: get parent ID, date, message, filesInCommit..
2. contructors: (init commit, regular commit, merge commit)
2. saveCommit(): creates new persistent file of commit object with hash code as name (similar to saveDog() in capers)
3. returnCommit(): returns deserialized commit object 
4. getHash(): returns hash code of any commit object
5. equals(): compares two commits (by their id)
6. getContent(): returns the blob id of a commit given its filename 
7. isCommitVersion(): returns true is file is the same version as this commit
8. updateCommitFiles(): reads staging area and makes necessary adjustments to files
9. putFileInCWD(): adds/overwrites file in CWD (used by checkout/reset commands)
10. getCurrentDate(): get current date and formats it properly
11. toString(): returns string representation of commit (used by log/global-log)

### COMMIT TREE
2. log()
3. commit()
4. checkoutFile()[first checkout]
5. checkoutFileInCommit() [second checkout]
7. merge()
8. currentCommit(): returns the id of the current commit
9. currentBranch(): returns name of current branch
10. updateCurrentHead(): updates the HEAD file with the name of the current branch
8. findSplit(): returns the commit id of the split point (uses LCA algorithm)
9. createConflictFile(): returns a file that contains merge conflict results
10. caseMerge(): takes the split, HEAD and branch commit files and returns an int that indicates what merge situation we're in
11. currentCommit(): returns the id of the current commit


### REPOSITORY
1. init()
2. globaLog()
3. find()
4. newBranch()
4. rm-branch()
5. checkoutBranch() [third checkout]
6. reset()


### BLOB
1. Blob(): creates blob and calls sha()
2. saveBlob(): saves blob as a serialized file with the hashcode as its file name
3. returnBlob(): given filename in CWD, returns Blob with its content
4. returnBlobContent(): given blobID, return contents as string 

### STAGE
1. add()
2. rm()
3. printStatus()
1. createIndex(): creates INDEX file (only used once by init)
4. clearStagingArea(): clears addition & removal collections
6. saveIndex(): serializes the index file after it's been modified 
7. returnIndex(): returns deserialized index file into a stage object
8. getUntrackedFiles(): returns list of files in CWD that haven't been staged or committed

###UTILS [PROVIDED]
1. sha1(): returns hash code
2. restrictedDelete(): Deletes FILE if it exists and is not a directory
3. readContents(): returns contents of file as byte array
4. readContentsAsString(): returns entire contents of file as a String
5. writeContents(): writes the result of concatenating the bytes in CONTENTS to FILE
6. readObject(): returns an object of type T read from file
7. writeObject(): write OBJ to FILE
8. plainFilenamesIn(): returns a list of the names of all plain files in the directory DIR
9. join(): returns the concatenation of FIRST and OTHERS into a File designator
10. error(): returns a GitletException (error)
11. message(): prints a message composed of MSG and ARGS


## Persistence
1. INDEX file: stores all info about the staging area: added/removed files, untracked files, untracked but unmodified files...
2. HEAD file: contains a path to the current branch 
3. branches folder: contains branch files with commit id's of each head 
4. commits folder: each commit object will be serialized into a file named after the commit's id
6. blobs folfer:  stores the serialized content of files

.gitlet
|--- HEAD.txt
|--- INDEX.txt
|--- branches
     |--- master
     |--- other
     
|--- commits
     |--- 93249vdf29924359dbg3245
     |--- 234sfjd34523jvsdj432534
     |--- ...
     
|--- blobs
     |--- s345j2h353jh452345j23j4
     |--- fj2345j2345n4523k4j5239
     |--- ...
