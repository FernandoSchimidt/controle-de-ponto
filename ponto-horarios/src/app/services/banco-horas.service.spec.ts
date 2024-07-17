import { TestBed } from '@angular/core/testing';

import { BancoHorasService } from './banco-horas.service';

describe('BancoHorasService', () => {
  let service: BancoHorasService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BancoHorasService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
