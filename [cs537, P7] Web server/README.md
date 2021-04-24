# P7: Web server multi-threaded
- converting the operation of a single threaded server to a multithreaded efficient web server, with shared memory segment receiving stats from threads, and can be read by another process for analysis. 
- Using pthreads, locks, condition variables; Synchronize access to a shared buffer with threads in producer/consumer relationships; use shared memory across cooperating processes; Signal handling.
- make webserver multithreaded; handle new input parameters; 
  1. Thread per request approach: spawn a new thread for every new http request, this solution would create an overhead for servicing each request.
  2. [x] Pool-of-threads approach: producer-consumer relationship with fixed-size pool of worker threads (defined on web server initialization).
      - producer thread (the main thread): creates a pool of worker threads (configurable from commandline), responsible for accepting new http connections over the network and placing the descriptor for this connection into a fixed-size buffer (configurable from commandline) and return to accepting more connections; 
      - Each worker thread is able to handle both static and dynamic requests. 
        - each thread is blocked until there is an http request for it to handle. A worker thread wakes when there is an http request in the queue, it performs the read on the network descriptor, obtains the specified content (by either reading the static file or executing the CGI process), and then returns the content to the client by writing to the descriptor. 
        - The worker thread then waits for another http request. if there are more requests than worker threads, then those requests will need to be buffered until there is a ready thread.
      - Synchronization: accesses to the shared buffer (between producer and threads) must be synchronized using condition variables.
        - producer thread must block and wait if the buffer is full.
        - a worker thread must wait if the buffer is empty. 
 - Stat process: webserver communicates with the stat process through a shared memory segment to display statistics about the server threads.
   - Each web-server thread will periodically write to the shared memory segment with updates about its recent behavior.
   - stat process collects this information (by reading from the shared memory segment) and periodically displays the information.

# Protocols, Libraries: 
- [HTTP/1.1](https://www.w3.org/Protocols/rfc2616/rfc2616.html)
- CGI protocol (way for web servers and server-side programs to interact through http protocol);
- routines useful for creating and synchronizing threads: 
  - pthread_create, 
  - pthread_mutex_init, pthread_mutex_lock,  pthread_mutex_unlock, 
  - pthread_cond_init, pthread_cond_wait, pthread_cond_signal. 
- Additionally to provided client, can use: netcat or curl to test the server.

# Todo list:
- [x] Will need to modify Makefile to compile, create, and clean stat_process.
- [x] download `~cs537-1/tests/p7`
- [x] download `~cs537-1/projects/web-server`
- [x] On CS labs: specify port numbers that are greater than about 2000 to avoid active ports. & use CS department VPN https://csl.cs.wisc.edu/docs/csl/2019-11-14-globalprotect-department-vpn/
- [x] handle paramter for # of threads to create.
- [x] Use condition variables, not spin-waiting techniques.
- [x] include proper headers to use shm_open() and mmap(). To use shm_open(), you need to use -lrt during compilation. 
  - rt is a library for real-time-related routines; 
  - prefix with -l is to tell the compiler that you want to link with this library.
- [x] Netcat utility allows you to send data over the connection at the exact time you want to. Helps in testing the buffer implementation.
  - e.g. `nc localhost 8080 -C` → `GET / HTTP/1.0` + 2x enter
- [ ] write wrapper functions for the new system routines that you call for error checking error codes.
- [ ] The web server forks a new process for each CGI process that it runs. This code shouldn't be modified.
- [ ] ensure that your web-server threads do not modify each other's data in the shared segment. 


## submission: 
- [ ] partners.txt: `cslogin1 wisclogin1 Lastname1 Firstname1`
- [ ] `~cs537-1/handin/<login>/p7/ontime/<web server files>`