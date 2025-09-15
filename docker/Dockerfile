# ------------------------------
# SmartServiceHub Full-Stack Dockerfile
# ------------------------------

# Base image
FROM archlinux:latest

# ------------------------------
# Environment variables
# ------------------------------
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
ENV TZ=UTC

# ------------------------------
# Working directory inside container
# ------------------------------
WORKDIR /SmartServiceApp

# ------------------------------
# Install essential packages
# ------------------------------
RUN pacman -Syu --noconfirm && \
    pacman -S --noconfirm base-devel git vim curl sudo \
    postgresql \
    jdk-openjdk maven \
    nodejs npm \
    nginx \
    lua \
    tor \
    && pacman -Scc --noconfirm

# ------------------------------
# Prepare PostgreSQL
# ------------------------------
RUN mkdir -p /var/lib/postgres/data && \
    chown -R postgres:postgres /var/lib/postgres

# ------------------------------
# Expose ports
# ------------------------------
EXPOSE 8080 5173 80

# ------------------------------
# Copy project files
# ------------------------------
COPY . .

# ------------------------------
# Optional: set default shell
# ------------------------------
CMD ["/bin/bash"]
