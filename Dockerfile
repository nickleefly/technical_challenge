FROM golang:alpine3.12 as builder

# Setup non root user with limited permission
#
RUN adduser \
    --disabled-password \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid 10001 \
    docker

# setup working directory
WORKDIR /src
# copy over files
COPY . .
# download dependencies
RUN go mod download

# be sure your go.sum file matches what you downloaded
RUN go mod verify

# Since cgo is disabled, Go doesn’t link with any system library. Since it doesn’t link with any system library, it can generate a static binary. Since it generates a static binary, that binary can work in the scratch image
RUN CGO_ENABLED=builder GOOS=linux GOARCH=amd64 go build -a -tags netgo -ldflags '-w -extldflags "-static"' -o /src/hello


FROM scratch
COPY --from=0 /src/hello .

# Expose port 3000
EXPOSE 3000

# use app user
USER docker:docker

ENTRYPOINT ["./hello"]
