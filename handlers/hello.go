package handlers

import (
	"net/http"
)

// Hello returns "Hello World!" to the client
func Hello(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("Hello world!"))
	return
}
