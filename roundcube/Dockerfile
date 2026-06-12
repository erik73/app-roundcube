ARG BUILD_FROM=ghcr.io/erik73/base:7.0.0
# hadolint ignore=DL3006
FROM ${BUILD_FROM}

# Set shell
SHELL ["/bin/bash", "-o", "pipefail", "-c"]
ARG S6_OVERLAY_VERSION="v3.2.3.0"
ARG ROUNDCUBE_VERSION="1.7.1"

# Build arguments
ARG BUILD_ARCH
ARG BUILD_DATE
ARG BUILD_DESCRIPTION
ARG BUILD_NAME
ARG BUILD_REF
ARG BUILD_REPOSITORY
ARG BUILD_VERSION

# Setup base
RUN apk add --no-cache \
    mariadb-client=11.8.8-r0 \
    nginx=1.30.2-r1 \ 
    php84=8.4.22-r0 \
    php84-fpm=8.4.22-r0 \
    php84-pecl-imap=1.0.3-r0 \
    php84-session=8.4.22-r0 \
    php84-mysqli=8.4.22-r0 \
    php84-opcache=8.4.22-r0 \
    php84-mbstring=8.4.22-r0 \
    php84-pdo_mysql=8.4.22-r0 \
    php84-curl=8.4.22-r0 \
    php84-xml=8.4.22-r0 \
    php84-dom=8.4.22-r0 \
    php84-intl=8.4.22-r0 \
    php84-ctype=8.4.22-r0 \
    php84-zip=8.4.22-r0 \
    php84-gd=8.4.22-r0 \
    php84-pecl-imagick=3.8.1-r0 \
    php84-xmlwriter=8.4.22-r0 \
    php84-fileinfo=8.4.22-r0 \
    php84-exif=8.4.22-r0 \
    php84-iconv=8.4.22-r0 \
    && curl -L -s "https://github.com/just-containers/s6-overlay/releases/download/${S6_OVERLAY_VERSION}/syslogd-overlay-noarch.tar.xz" \
        | tar -C / -Jxpf - \
    && sed -i 's#^ + .*$# + -^auth\\. -^authpriv\\. -mail\\. $T ${dir}/everything#' /etc/s6-overlay/s6-rc.d/syslogd-log/run \
    && sed -i 's#^backtick .*$#backtick -D "n20 s1000000 T 1" line { printcontenv S6_LOGGING_SCRIPT }#' /etc/s6-overlay/s6-rc.d/syslogd-log/run \
    && sed -i 's#^s6-socklog .*$#s6-socklog -d3 -U -t3000 -x /run/systemd/journal/dev-log#' /etc/s6-overlay/s6-rc.d/syslogd/run \
    && mkdir /var/www/roundcube \
    && curl -L -s "https://github.com/roundcube/roundcubemail/releases/download/${ROUNDCUBE_VERSION}/roundcubemail-${ROUNDCUBE_VERSION}-complete.tar.gz" \
        | tar zxvf - -C /var/www/roundcube --strip-components=1 \
        && rm -f -r \
        /tmp/* \
        /var/www/roundcube/installer \
    && mkdir -p /run/systemd/journal

# Copy data for app
COPY rootfs /

# Labels
LABEL \
    io.hass.name="${BUILD_NAME}" \
    io.hass.description="${BUILD_DESCRIPTION}" \
    io.hass.arch="${BUILD_ARCH}" \
    io.hass.type="addon" \
    io.hass.version=${BUILD_VERSION} \
    maintainer="Erik Hilton <erikhilton73@gmail.com>" \
    org.opencontainers.image.title="${BUILD_NAME}" \
    org.opencontainers.image.description="${BUILD_DESCRIPTION}" \
    org.opencontainers.image.vendor="Erik73 Hassio-Addons" \
    org.opencontainers.image.authors="Erik Hilton <erikhilton73@gmail.com>" \
    org.opencontainers.image.licenses="MIT" \
    org.opencontainers.image.url="https://github.com/erik73/hassio-addons" \
    org.opencontainers.image.source="https://github.com/${BUILD_REPOSITORY}" \
    org.opencontainers.image.documentation="https://github.com/${BUILD_REPOSITORY}/blob/main/README.md" \
    org.opencontainers.image.created=${BUILD_DATE} \
    org.opencontainers.image.revision=${BUILD_REF} \
    org.opencontainers.image.version=${BUILD_VERSION}
