INSERT INTO articles (
    id,
    body,
    created,
    is_approved,
    tag,
    title,
    updated,
    author_id
  )
VALUES (
    9001,
    'I love Java very much. Especially I love implementing interfaces!',
    '2022-1-1',
    0,
    '{"items":[{"label":"programming","probability":0.9911031126976013},{"label":"sport","probability":0.003971323370933533},{"label":"reading","probability":0.003238707548007369},{"label":"dancing","probability":0.001686922158114612}]}',
    'On Java',
    '2022-1-1',
    NULL
  );
INSERT INTO articles (
    id,
    body,
    created,
    is_approved,
    tag,
    title,
    updated,
    author_id
  )
VALUES (
    9002,
    'Dancing all night! Friday night!',
    '2022-1-1',
    0,
    '{"items":[{"label":"dancing","probability":0.9911031126976013},{"label":"sport","probability":0.003971323370933533},{"label":"reading","probability":0.003238707548007369},{"label":"programming","probability":0.001686922158114612}]}',
    'Here we go',
    '2022-1-1',
    NULL
  );