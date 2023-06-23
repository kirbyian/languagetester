package com.kirby.languagetester.utils;


import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

public class GitHubDirectoryExample {
    public static void main(String[] args) {
        String repositoryUrl = "https://github.com/ian-hamlin/verb-data.git";
        String directoryPath = "json/portuguese/content";

        // Clone the repository to a local directory
        File localDir = new File("path/to/local/directory");
        cloneRepository(repositoryUrl, localDir);

        // Iterate through the files in the directory
        File directory = new File(localDir, directoryPath);
        iterateDirectory(directory);
    }

    private static void cloneRepository(String repositoryUrl, File localDir) {
        try {
            System.out.println("Cloning repository...");
            CloneCommand cloneCommand = Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setDirectory(localDir);
            Git git = cloneCommand.call();
            System.out.println("Repository cloned successfully");
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    private static void iterateDirectory(File directory) {
        try (Repository repository =new RepositoryBuilder().findGitDir(directory).build();
             Git git = new Git(repository);
             RevWalk revWalk = new RevWalk(repository)) {

            // Get the latest commit
            Ref head = repository.exactRef("HEAD");
            RevCommit commit = revWalk.parseCommit(head.getObjectId());
            RevTree tree = commit.getTree();

            // Start the TreeWalk at the specified directory
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
       //     treeWalk.setFilter(path 

            // Iterate through the files in the directory
            while (treeWalk.next()) {
                File file = new File(directory, treeWalk.getPathString());
                if (file.isFile()) {
                    System.out.println("File: " + file.getPath());
                    // Process the file as needed
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
