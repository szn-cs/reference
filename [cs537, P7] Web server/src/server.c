#include "helper.h"
#include "request.h"

// Parse arguments
void getargs(int *port, int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <port>\n", argv[0]);
        exit(1);
    }
    *port = atoi(argv[1]);
}

/**
 * @brief  A very, very simple web server. Repeatedly handles HTTP requests sent
 * to this port number. Most of the work is done within routines written in
 * request.c
 *
 * usage: `server [port_num] [threads] [buffers] [shm_name]`
 *      client requests:
 *      - `client localhost 5003 /home.html`
 *      - `client localhost 5003 /output.cgi?2`
 *
 * @param argc args count
 * @param argv args values
 * @arg @param port_num port number that the web server should listen on
 * @arg @param threads # of worker threads that should be created
 * @arg @param buffers # of request connections can be accepted at one time
 * @arg @param shm_name name of shared memory object
 * @return int 0 on success, otherwise 1
 */
int main(int argc, char *argv[]) {
    int listenfd, connfd, port, clientlen;
    struct sockaddr_in clientaddr;

    getargs(&port, argc, argv);

    // arguments validation
    if (threads < 1 || buffers < 1) goto fail;

    //
    // CS537 (Part B): Create & initialize the shared memory region...
    // will create <shm_name> at /dev/shm/  in CSL machines

    //
    // CS537 (Part A): Create some threads...
    //

    listenfd = Open_listenfd(port);
    while (1) {
        clientlen = sizeof(clientaddr);
        // accept connectio and get file descriptor
        connfd = Accept(listenfd, (SA *)&clientaddr, (socklen_t *)&clientlen);

        //
        // CS537 (Part A): In general, don't handle the request in the main
        // thread. Save the relevant info in a buffer and have one of the worker
        // threads do the work. Also let the worker thread close the connection.
        //
        requestHandle(connfd);
        Close(connfd);
    }

    return 0;

fail:
    return 1;
}
